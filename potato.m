function carbs = potato(a, b, c);
%UNTITLED Summary of this function goes here

prompt = 'What is the ellipsoid height in centimeters? ';
a = input(prompt);

prompt = 'What is the ellipsoid length in centimeters? ';
b = input(prompt);

prompt = 'What is the ellipsoid width in centimeters? ';
c = input(prompt);

ellipsoid_volume =(4/3)*pi*(a/2)*(b/2)*(c/2);

%density = 0.641; %in g/cm^3
carb_density_mass = 0.1713; %in carbs/gm
carb_density_volume = 0.11; %in carbs/cm^3


carbs = ellipsoid_volume*carb_density_volume;

prompt = 'What is the mass in grams? ';
mass = input(prompt);

percent_error = abs((mass*carb_density_mass - carbs)/(mass*carb_density_mass)*100)

display carbs
        
end
