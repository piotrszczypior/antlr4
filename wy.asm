    MOV A,#0
    PUSH A
    MOV A,#1
    POP B
    PUSH B
    MOV B,#0
    CMP A,B
    JE label_and_false_0
    POP B
    MOV A,#0
    CMP B,A
    JE label_and_false_0
    MOV A,#1
    JMP label_and_end_0

    label_and_false_0:
        MOV A,#0

    label_and_end_0:
        JE label_else_0
        DD a
        MOV A,#3
        MOV [a],A

        JMP label_endif_0

    label_else_0:

    label_endif_0:
