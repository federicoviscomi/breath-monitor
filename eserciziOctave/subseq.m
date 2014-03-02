%restituisce la sottosequenza di seq che contiene solo i valori in posizioni
%multiple di n
%seq deve esere un vettore riga cioe' tale che size(seq)(1)=1

function y = subseq(seq, n)
  if (n<=0 | ~isvector(seq) | size(seq)(1)~=1)
    printf('invalid arguments\n');
    y=[];
    return;
  endif
    size=size(seq)(2);
    y=zeros(1,ceil(size/n));
    j=1;
    for i=1:size
      if (mod(i,n)==0)
	y(j)=seq(i);
	j++;
      endif
    endfor
endfunction