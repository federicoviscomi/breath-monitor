clear all;
qmf = MakeONFilter('Coiflet',2);
n=64;
x=abs([1:n]-(n+1)*2/3); 
wc = FWT_PO(x,1,qmf); 

plot(x);
hold on;
plot(wc, '-r');
xc = IWT_PO(wc,1,qmf); 
norm(xc-x)