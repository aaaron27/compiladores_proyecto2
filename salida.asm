.data
newline: .asciiz "\n"
true_msg: .asciiz "true"
false_msg: .asciiz "false"
str_0: .asciiz "entra al if"
str_1: .asciiz "!|"

.text
.globl main

# =          "Hola  endl ~&|! endl & ?  mundo"            _str_     
    # String loading not fully implemented inline
    sw $t0, -8($fp)
# =          20                    i         
    li $t0, 20
    sw $t0, -12($fp)
# LABEL      null                  L0        
L0:
# >=         i          10         $t0       
    lw $t0, -12($fp)
    li $t1, 10
    sge $t2, $t0, $t1
    sw $t2, -16($fp)
# IF_FALSE   $t0                   L1        
    lw $t0, -16($fp)
    beqz $t0, L1
# LABEL      null                  L2        
L2:
# =          234                   _var_2    
    li $t0, 234
    sw $t0, -20($fp)
# -          _var_2     1          $t1       
    lw $t0, -20($fp)
    li $t1, 1
    sub $t2, $t0, $t1
    sw $t2, -24($fp)
# =          $t1                   var       
    lw $t0, -24($fp)
    sw $t0, -28($fp)
# !=         15         12.2       $t2       
    li $t0, 15
    li $t1, 12.2
    sne $t2, $t0, $t1
    sw $t2, -32($fp)
# *          34         35         $t3       
    li $t0, 34
    li $t1, 35
    mul $t2, $t0, $t1
    sw $t2, -36($fp)
# ==         12         $t3        $t4       
    li $t0, 12
    lw $t1, -36($fp)
    seq $t2, $t0, $t1
    sw $t2, -40($fp)
# NOT        $t4                   $t5       
    lw $t0, -40($fp)
    xori $t0, $t0, 1
    sw $t0, -44($fp)
# AND        $t2        $t5        $t6       
    lw $t0, -32($fp)
    lw $t1, -44($fp)
    and $t2, $t0, $t1
    sw $t2, -48($fp)
# IF         $t6                   L2        
    lw $t0, -48($fp)
    bnez $t0, L2
# GOTO       null                  L3        
    j L3
# LABEL      null                  L3        
L3:
# <=         var        5.6        $t7       
    lw $t0, -28($fp)
    li $t1, 5.6
    sle $t2, $t0, $t1
    sw $t2, -52($fp)
# IF_FALSE   $t7                   L5        
    lw $t0, -52($fp)
    beqz $t0, L5
# PRINT      "entra al if"            null      
    li $v0, 4
    la $a0, str_0
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# GOTO       null                  L4        
    j L4
# LABEL      null                  L5        
L5:
# LABEL      null                  L4        
L4:
# IF_FALSE   true                  L7        
    li $t0, 1
    beqz $t0, L7
# PRINT      var                   null      
    lw $a0, -28($fp)
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# GOTO       null                  L6        
    j L6
# LABEL      null                  L7        
L7:
# ==         _otra_     'a'        $t8       
    lw $t0, -56($fp)
    lw $t1, -60($fp)
    seq $t2, $t0, $t1
    sw $t2, -64($fp)
# IF_FALSE   $t8                   L9        
    lw $t0, -64($fp)
    beqz $t0, L9
# PRINT      "!|"                  null      
    li $v0, 4
    la $a0, str_1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# GOTO       null                  L8        
    j L8
# LABEL      null                  L9        
L9:
# ==         _otra_     'b'        $t9       
    lw $t0, -56($fp)
    lw $t1, -68($fp)
    seq $t2, $t0, $t1
    sw $t2, -72($fp)
# IF_FALSE   $t9                   L10       
    lw $t0, -72($fp)
    beqz $t0, L10
# =          10                    _otra123133442_
    li $t0, 10
    sw $t0, -76($fp)
# GOTO       null                  L8        
    j L8
# LABEL      null                  L10       
L10:
# *          -10.5      -1.9       $t10      
    li $t0, -10.5
    li $t1, -1.9
    mul $t2, $t0, $t1
    sw $t2, -80($fp)
# =          $t10                  _otra_    
    lw $t0, -80($fp)
    sw $t0, -56($fp)
# LABEL      null                  L8        
L8:
# LABEL      null                  L6        
L6:
# -          i          1          i         
    lw $t0, -12($fp)
    li $t1, 1
    sub $t2, $t0, $t1
    sw $t2, -12($fp)
# GOTO       null                  L0        
    j L0
# LABEL      null                  L1        
L1:
# =          -0.01                 _wawa_    
    li $t0, -0.01
    sw $t0, -84($fp)
# =          '!'                   _miChar_  
    lw $t0, -88($fp)
    sw $t0, -92($fp)
# =          '!'                   _miChar2_ 
    lw $t0, -88($fp)
    sw $t0, -96($fp)
# ARR_STORE  _arr_      0          1         
    li $t0, 0
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 1
    sw $t2, 0($t1)
# ARR_STORE  _arr_      4          5         
    li $t0, 4
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 5
    sw $t2, 0($t1)
# ARR_STORE  _arr_      8          10        
    li $t0, 8
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 10
    sw $t2, 0($t1)
# ARR_STORE  _arr_      12         2         
    li $t0, 12
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 2
    sw $t2, 0($t1)
# ARR_STORE  _arr_      16         4         
    li $t0, 16
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 4
    sw $t2, 0($t1)
# ARR_STORE  _arr_      20         5         
    li $t0, 20
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 5
    sw $t2, 0($t1)
# ARR_STORE  _arr_      24         20        
    li $t0, 24
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 20
    sw $t2, 0($t1)
# ARR_STORE  _arr_      28         40        
    li $t0, 28
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 40
    sw $t2, 0($t1)
# ARR_STORE  _arr_      32         50        
    li $t0, 32
    addiu $t1, $fp, -100
    sub $t1, $t1, $t0
    li $t2, 50
    sw $t2, 0($t1)
# READ_INT   null                  _s1_      
    li $v0, 5
    syscall
    sw $v0, -104($fp)
# PRINT      _b1_                  null      
    lw $a0, -108($fp)
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# PRINT      true                  null      
    li $v0, 4
    la $a0, true_msg
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# PRINT      -6.7                  null      
    li $v0, 2
    li.s $f12, -6.7
    syscall
    li $v0, 4
    la $a0, newline
    syscall
# =          56.6                  fl1       
    li $t0, 56.6
    sw $t0, -112($fp)
# %          45         76         $t11      
    li $t0, 45
    li $t1, 76
    rem $t2, $t0, $t1
    sw $t2, -116($fp)
# +          1          $t11       $t12      
    li $t0, 1
    lw $t1, -116($fp)
    add $t2, $t0, $t1
    sw $t2, -120($fp)
# =          $t12                  fl2       
    lw $t0, -120($fp)
    sw $t0, -124($fp)
# -          10         11         $t13      
    li $t0, 10
    li $t1, 11
    sub $t2, $t0, $t1
    sw $t2, -128($fp)
# =          $t13                  arr       
    lw $t0, -128($fp)
    sw $t0, -132($fp)
# ^          4.5        -0.005     $t14      
# =          $t14                  fl1       
    lw $t0, -136($fp)
    sw $t0, -112($fp)
# !=         6.7        8.9        $t15      
    li $t0, 6.7
    li $t1, 8.9
    sne $t2, $t0, $t1
    sw $t2, -140($fp)
# =          $t15                  bl0       
    lw $t0, -140($fp)
    sw $t0, -144($fp)
# !=         true       false      $t16      
    li $t0, 1
    li $t1, 0
    sne $t2, $t0, $t1
    sw $t2, -148($fp)
# =          $t16                  bl0       
    lw $t0, -148($fp)
    sw $t0, -144($fp)
# >=         543.21     fl1        $t17      
    li $t0, 543.21
    lw $t1, -112($fp)
    sge $t2, $t0, $t1
    sw $t2, -152($fp)
# >          20         56         $t18      
    li $t0, 20
    li $t1, 56
    slt $t2, $t1, $t0
    sw $t2, -156($fp)
# NOT        $t18                  $t19      
    lw $t0, -156($fp)
    xori $t0, $t0, 1
    sw $t0, -160($fp)
# AND        false      $t19       $t20      
    li $t0, 0
    lw $t1, -160($fp)
    and $t2, $t0, $t1
    sw $t2, -164($fp)
# OR         $t17       $t20       $t21      
    lw $t0, -152($fp)
    lw $t1, -164($fp)
    or $t2, $t0, $t1
    sw $t2, -168($fp)
# =          $t21                  bl1       
    lw $t0, -168($fp)
    sw $t0, -172($fp)
# +          1          2          $t22      
    li $t0, 1
    li $t1, 2
    add $t2, $t0, $t1
    sw $t2, -176($fp)
