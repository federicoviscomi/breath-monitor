clear all;
t=linspace(-pi, pi, 20);
x=sin(t);

tt=linspace(-pi, pi, 600);
xx=sin(tt);

stairs(t,x);
plot(tt,xx,'k');