.data
newline: .asciiz "\n"
true_msg: .asciiz "true"
false_msg: .asciiz "false"

.text
.globl main

# LABEL      null                  main      
main:
    move $fp, $sp
    sub $sp, $sp, 500
# =          0                     i         
    li $t0, 0
    sw $t0, -8($fp)
# LABEL      null                  L0        
L0:
# <          i          5          $t0       
    lw $t0, -8($fp)
    li $t1, 5
    slt $t2, $t0, $t1
    sw $t2, -12($fp)
# IF_FALSE   $t0                   L1        
    lw $t0, -12($fp)
    beqz $t0, L1
# PRINT      i                     null      
    lw $a0, -8($fp)
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# +          i          1          i         
    lw $t0, -8($fp)
    li $t1, 1
    add $t2, $t0, $t1
    sw $t2, -8($fp)
# GOTO       null                  L0        
    j L0
# LABEL      null                  L1        
L1:
# DECLARE_ARRAY _arr_      36         null      
    # Reservado espacio para array '_arr_': 36 bytes
# ARR_STORE  _arr_      0          1         
    li $t0, 0
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 1
    sw $t2, 0($t1)
# ARR_STORE  _arr_      4          5         
    li $t0, 4
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 5
    sw $t2, 0($t1)
# ARR_STORE  _arr_      8          10        
    li $t0, 8
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 10
    sw $t2, 0($t1)
# ARR_STORE  _arr_      12         2         
    li $t0, 12
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 2
    sw $t2, 0($t1)
# ARR_STORE  _arr_      16         4         
    li $t0, 16
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 4
    sw $t2, 0($t1)
# ARR_STORE  _arr_      20         5         
    li $t0, 20
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 5
    sw $t2, 0($t1)
# ARR_STORE  _arr_      24         20        
    li $t0, 24
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 20
    sw $t2, 0($t1)
# ARR_STORE  _arr_      28         40        
    li $t0, 28
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 40
    sw $t2, 0($t1)
# ARR_STORE  _arr_      32         50        
    li $t0, 32
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    li $t2, 50
    sw $t2, 0($t1)
# *          0          3          $t1       
    li $t0, 0
    li $t1, 3
    mul $t2, $t0, $t1
    sw $t2, -52($fp)
# +          $t1        0          $t2       
    lw $t0, -52($fp)
    li $t1, 0
    add $t2, $t0, $t1
    sw $t2, -56($fp)
# *          $t2        4          $t3       
    lw $t0, -56($fp)
    li $t1, 4
    mul $t2, $t0, $t1
    sw $t2, -60($fp)
# ARR_LOAD   _arr_      $t3        $t4       
    lw $t0, -60($fp)
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    lw $t2, 0($t1)
    sw $t2, -64($fp)
# PRINT      $t4                   null      
    lw $a0, -64($fp)
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# *          2          3          $t5       
    li $t0, 2
    li $t1, 3
    mul $t2, $t0, $t1
    sw $t2, -68($fp)
# +          $t5        1          $t6       
    lw $t0, -68($fp)
    li $t1, 1
    add $t2, $t0, $t1
    sw $t2, -72($fp)
# *          $t6        4          $t7       
    lw $t0, -72($fp)
    li $t1, 4
    mul $t2, $t0, $t1
    sw $t2, -76($fp)
# ARR_LOAD   _arr_      $t7        $t8       
    lw $t0, -76($fp)
    addiu $t1, $fp, -16
    sub $t1, $t1, $t0
    lw $t2, 0($t1)
    sw $t2, -80($fp)
# PRINT      $t8                   null      
    lw $a0, -80($fp)
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# PRINT      27                    null      
    li $a0, 27
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# +          1          2          $t9       
    li $t0, 1
    li $t1, 2
    add $t2, $t0, $t1
    sw $t2, -84($fp)
# EXIT       null                  null      
    li $v0, 10
    syscall
