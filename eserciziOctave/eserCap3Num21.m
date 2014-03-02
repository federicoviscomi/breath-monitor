clear all
x=linspace(-3,4,101);
f= 3 .* arrayfun(@fi,x.+1) .- 2.* arrayfun(@fi, x) .+ arrayfun(@fi,x.-1).+arrayfun(@fi,x.-2);
g= arrayfun(@fi,2 .*x.+3) .-  arrayfun(@fi, 2 .*x.+2) .+ 2 .*arrayfun(@fi,2 .* x) .- arrayfun(@fi,2 .*x.-3);
plot(x,f,'o',x,g,'o');