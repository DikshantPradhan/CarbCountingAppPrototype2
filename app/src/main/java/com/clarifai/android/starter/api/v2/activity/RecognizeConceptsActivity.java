package com.clarifai.android.starter.api.v2.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.OnClick;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.clarifai.android.starter.api.v2.App;
import com.clarifai.android.starter.api.v2.ClarifaiUtil;
import com.clarifai.android.starter.api.v2.R;
import com.clarifai.android.starter.api.v2.adapter.RecognizeConceptsAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class RecognizeConceptsActivity extends BaseActivity {

  public static final int PICK_IMAGE = 100;

  //ADDED FOR  DATABASE FUNCTIONALITY
  Map<String, List<String>> db; // IMPORTANT MEMBER : database

  List<String> keys; // IMPORTANT MEMBER : keys to database


  // the list of results that were returned from the API
  @BindView(R.id.resultsList) RecyclerView resultsList;

  // interactable list of results


  // the view where the image the user selected is displayed
  @BindView(R.id.image) ImageView imageView;

  // switches between the text prompting the user to hit the FAB, and the loading spinner
  @BindView(R.id.switcher) ViewSwitcher switcher;

  // the FAB that the user clicks to select an image
  @BindView(R.id.fab) View fab;

  @NonNull private final RecognizeConceptsAdapter adapter = new RecognizeConceptsAdapter();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override protected void onStart() {
    super.onStart();

    resultsList.setLayoutManager(new LinearLayoutManager(this));
    resultsList.setAdapter(adapter);
  }

  @OnClick(R.id.fab)
  void pickImage() {
    startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), PICK_IMAGE);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != RESULT_OK) {
      return;
    }
    switch(requestCode) {
      case PICK_IMAGE:
        final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
        if (imageBytes != null) {
          onImagePicked(imageBytes);
        }
        break;
    }
  }

  private void onImagePicked(@NonNull final byte[] imageBytes) {
    // Now we will upload our image to the Clarifai API
    setBusy(true);

    // Make sure we don't show a list of old concepts while the image is being uploaded
    adapter.setData(Collections.<Concept>emptyList());

    new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
      @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
        // The default Clarifai model that identifies concepts in images
        final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().foodModel();

        // Use this model to predict, with the image that the user just selected as the input
        return generalModel.predict()
                .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                .executeSync();
      }

      @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
        setBusy(false);
        if (!response.isSuccessful()) {
          showErrorSnackbar(R.string.error_while_contacting_api);
          return;
        }
        final List<ClarifaiOutput<Concept>> predictions = response.get();
        if (predictions.isEmpty()) {
          showErrorSnackbar(R.string.no_results_from_api);
          return;
        }
        adapter.setData(predictions.get(0).data());

        // INSERT METHOD FOR USER SELECTION OF FOOD

        // ADDED FOR DATABASE
        try {
          readCSVToMap("ABBREV_2.txt");
        }
        catch (Exception e){
          Log.d("Failure", "CSV not read into database");
        }
        String exampleResult = predictions.get(0).data().get(0).name();
        final List<String> list = listOfKeys(exampleResult);

        // change this line to take in user input
        final String key2 = list.get(0); // arbitrary selection of key

        final List<String> val = db.get(key2);
        final String message = String.valueOf(val.get(6)); //index 6 contains carb info
        Log.d("Output", message);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
      }

      private void showErrorSnackbar(@StringRes int errorString) {
        Snackbar.make(
                root,
                errorString,
                Snackbar.LENGTH_INDEFINITE
        ).show();
      }
    }.execute();
  }


  @Override protected int layoutRes() { return R.layout.activity_recognize; }

  private void setBusy(final boolean busy) {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        switcher.setDisplayedChild(busy ? 1 : 0);
        imageView.setVisibility(busy ? GONE : VISIBLE);
        fab.setEnabled(!busy);
      }
    });
  }

  //ADDED FUNCTION FOR DATABASE
  protected void readCSVToMap(String filename) throws Exception{ // IMPORTANT FUNCTION
    db = new HashMap<String, List<String>>();
    keys = new ArrayList<String>();
    InputStream is = getAssets().open("ABBREV_2.txt");
    //File f = new File(path.toURI());
    //File f = new File(path.getFile());
    BufferedReader in = new BufferedReader(new InputStreamReader(is));//new BufferedReader(new FileReader("ABBREV_2.txt"));
    String line = "";
    while ((line = in.readLine()) != null) {
      String parts[] = line.split("\t");
      List<String> nutrition = new ArrayList();
      for (int i = 1; i < parts.length; i++){
        nutrition.add(parts[i]);
      }
      db.put(parts[0], nutrition);
      keys.add(parts[0]);
    }
    in.close();

  }

  protected List<String> listOfKeys(String food){ // IMPORTANT FUNCTION
    food = food.toLowerCase();
    List<String> trueKeys = new ArrayList<String>();

    for (String key: keys){ // may want to pass in keys as parameter?
      if (key.toLowerCase().contains(food)){
        trueKeys.add(key);
      }
    }

    return trueKeys;
  }

}
