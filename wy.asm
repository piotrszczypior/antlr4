    DD x
    MOV A,#2
    MOV [x],A
    DD y
    MOV A,#3
    MOV [y],A
        MOV A,#3
        PUSH A
        MOV A,[y]
        POP B
        MUL A, B
        PUSH A
        MOV A,#3
        PUSH A
        MOV A,[x]
        POP B
        ADD A, B
        POP B
        ADD A, B
