pointsPerUnit=33;
x=linspace(-2*pi,2*pi,4*pi*pointsPerUnit);
y=[zeros(1,pi*pointsPerUnit), 0, ones(1,2*pi*pointsPerUnit), zeros(1,pi*pointsPerUnit)];
plot(x,y)
axis([-3*pi, 3*pi, -2, 2]);
xlabel ("x");
ylabel ("y");
%text (pi, 0.7, "arbitrary text");
legend ("rectangular wave");
figure;
plot(x,abs(fft(y)))