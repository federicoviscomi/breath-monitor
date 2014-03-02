clear all;
qmf = MakeONFilter('Coiflet',3);
x=linspace(-2,3,2^5);
y=sin(8 * pi .*x) .* cos(3*pi .* x) .+ rand();
wc = FWT_PO(y,1,qmf); 
plot(x,y);
hold on;
plot(x,wc, '-r');
%  xc = IWT_PO(wc,1,qmf); 
%  norm(xc-x)