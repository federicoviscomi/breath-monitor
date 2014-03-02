% disegna il grafico tempo/ampiezza di un file .wav

[Y, FS, BITS]=wavread('Normal vesicular.wav');
x=linspace(-1,1,size(Y)(1));
f=fft(Y);
%z=linspace(-10,10,size(f)(1));
plot(x(1:500), Y(1:500));
figure;
plot(f(1:500))