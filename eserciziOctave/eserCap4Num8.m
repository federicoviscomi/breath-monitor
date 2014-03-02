clear all;
t=linspace(0,1,101);
f=1 .- t .^ 2;
plot(t,f,t,abs(fft(f)));
