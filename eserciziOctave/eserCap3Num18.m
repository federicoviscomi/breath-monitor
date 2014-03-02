clear all;
x=[zeros(1,6), ones(1,7),zeros(1,2)];
y=[zeros(1,2), (-8+2:1:0), zeros(1,6)];
con=conv(x,y);
stem(con);
title('Covolution of x and y');