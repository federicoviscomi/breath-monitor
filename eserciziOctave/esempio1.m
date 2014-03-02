X=[-4096:4095]/64;
Y = X .^2;
# plot(X, Y);

F = fft(Y);
S = [0:2047]/2048;

function points = approximate(input, count)
    size    = size(input)(2);
    fourier = [fft(input)(1:count) zeros(1, size-count)];
    points  = ifft(fourier);
endfunction;

plot(X, Y, X, approximate(Y, 10));