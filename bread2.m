function carbs = bread2(a, b, c);
%UNTITLED Summary of this function goes here

% the only difference between this function is that the height of the prism
% (width of slice) was assumed to be nearly constant and taken to be the
% average of multiple measurements of difference slices. This way, as far
% as the app is concerned, only one picture would be needed
avg_bread_slice = 1.12;

%prompt = 'What is the rectangular prism height in centimeters? ';
a = avg_bread_slice;

prompt = 'What is the rectangular prism length in centimeters? ';
b = input(prompt);

prompt = 'What is the rectangular prism width in centimeters? ';
c = input(prompt);

rectangular_prism_volume = a*b*c;

density = 0.19; %in g/cm^3
carb_density = 0.501; %in g Carb per gram food

est_mass = density*rectangular_prism_volume;

carbs = est_mass*carb_density;

prompt = 'What is the mass in grams? ';
mass = input(prompt);

est_carbs = carbs

true_carbs = mass*carb_density

percent_error = abs((true_carbs - carbs)/(true_carbs)*100)
        
end