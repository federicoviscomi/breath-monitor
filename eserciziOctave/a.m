N=7;
x=linspace(-2^6,2^6,2^7);
for i=1:2^N
 sleep(0.3);
  n=2^N; wc=zeros(1,n); wc(i)=1; xc=IWT_PO(wc,1,qmf); plot(x,xc);
endfor
