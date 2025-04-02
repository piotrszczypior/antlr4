    MOV A,#1
    PUSH A
    MOV A,#1
    POP B
    CMP A,B

    JNE label_comparison_0
    MOV A,#1
    JMP label_comparsion_end_0

    label_comparison_0:
        MOV A,#0

    label_comparsion_end_0:
        JE label_else_0
        DD a
        MOV A,#5
        MOV [a],A

        JMP label_endif_0

    label_else_0:

    label_endif_0:
    MOV A,#0
    PUSH A
    MOV A,#1
    POP B
    CMP A,B

    JNE label_comparison_1
    MOV A,#1
    JMP label_comparsion_end_1

    label_comparison_1:
        MOV A,#0

    label_comparsion_end_1:
        JE label_else_1
        DD b
        MOV A,#1
        MOV [b],A

        JMP label_endif_1

    label_else_1:

    label_endif_1:
    MOV A,#0
    PUSH A
    MOV A,#1
    POP B
    CMP A,B
        JE label_else_2
        DD c
        MOV A,#2
        MOV [c],A

        JMP label_endif_2

    label_else_2:

    label_endif_2:
    MOV A,#1
    PUSH A
    MOV A,#1
    POP B
    CMP A,B
        JE label_else_3
        DD d
        MOV A,#3
        MOV [d],A

        JMP label_endif_3

    label_else_3:

    label_endif_3:
