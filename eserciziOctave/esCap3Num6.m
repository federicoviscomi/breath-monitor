clear all;
x=linspace(0,2*pi,256);
y=(exp(-(cos(x).^2))).*(sin(2.*x).+2.*cos(4.*x)+0.4.*sin(x).*sin(50.*x));

for noiseStartFrequency=1:size(x)(2)
  f=[fft(y)(1:noiseStartFrequency), zeros(1,size(x)(2)-noiseStartFrequency)];
  plot(x,y,x,ifft(f));
  fprintf(stderr,'%d\n',noiseStartFrequency);
  sleep(0.5);
endfor