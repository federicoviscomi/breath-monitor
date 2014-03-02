function a=removeLow(b, threshold)
  s=size(b,1);
  if (s==1)
    s=size(b,2);
  endif
  a=zeros(1,s);
  for i=1:s
    if(abs(b(i))>=threshold)
      a(i)=b(i);
    endif
  endfor
endfunction