clear all;
w=linspace(-pi,pi,600);
z=exp(i*w);
n=2;
h=ones(1,2*n+1)/(2*n+1);
H=z .^n .* polyval(h, 1./z);
norm(imag(H));
plot(w,real(h));