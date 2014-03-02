clear all;
y = wavread('Normal vesicular.wav');
size=size(y)(1);
x=linspace(0,size,size);
a=1;
b=100000;
y=y(a:b);
x=x(a:b);
%plot(x,y)
plot(x,abs(fft(y)));