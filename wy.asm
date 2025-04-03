    square:
        PUSH BP
        DD x
        MOV A, [BP+1]
        MOV [x], A
        DD y
        MOV A, [BP+2]
        MOV [y], A

            DD a
            MOV A,[x]
            MOV [a],A

        MOV BP, SP

        POP BP
        RET
        MOV A,#2
        PUSH A
        MOV A,#11
        POP B
        ADD A, B
        PUSH A
        MOV A,#2
        PUSH A
        CALL square
        POP B
        POP B

