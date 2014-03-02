k=10;
n=[0: 29];
N=64;
x=cos(2*pi*k*n/N);
X1=abs(fft(x,N));
F1=[0:N-1];
plot(F1,X1,'-x')
plot(F1(1:N/2), X1(1:N/2), '-x')