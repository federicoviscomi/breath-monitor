% implementa x(1:n)
%
function y = take(x, n)
  y=[];
  i=1;
  if isvector(x)
    if (size(x)(1)==1)
      size=size(x)(2);
    else
      size=size(x)(1);
    endif
    while (i <= n) & (i<=size)
      y=[y,x(i)];
      i=i+1;
    endwhile
  endif
endfunction