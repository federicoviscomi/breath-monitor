x1=linspace(-3*pi, -pi, 201);
x2=linspace(-pi, pi, 201);
x3=linspace(pi, 3*pi, 201);
y1=x2;
y2=x2;
y3=x2;

function y=S(x,k)
  y=0;
  while (k>=1)
    y=y+ (sin(k*x)*2*(-1)^(k+1))/k;
    k=k-1;
  endwhile
endfunction

x=linspace(-3*pi, 3*pi, 303);
y=S(x,10);
%plot(x1, y1, x2, y2, x3, y3,x,y);

plot(abs(fft(y1)))