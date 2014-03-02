clear all;
x=linspace(-7,7,14*153);
y=(exp(-(cos(x).^2))).*(sin(2.*x).+2.*cos(4.*x)+0.4.*sin(x).*sin(50.*x));
plot(x,y);