package com.example.subscribebook.services;

public class DecodeService {
    public int decode(String id) {
        int a=0;
        for (int i = id.length()-1; i >= 0; i--) {
            a *= 10;
            a += getIntForSymbol(id.charAt(i));
        }
        return a;
    }

    public int getIntForSymbol(char a) {
        if(a=='a') return 0;
        if(a=='b') return 1;
        if(a=='c') return 2;
        if(a=='d') return 3;
        if(a=='e') return 4;
        if(a=='f') return 5;
        if(a=='g') return 6;
        if(a=='h') return 7;
        if(a=='i') return 8;
        if(a=='j') return 9;
        return -1;
    }
}
