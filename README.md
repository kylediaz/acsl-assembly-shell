# ACSL Assembly Shell

Shell, compiler, and interpreter for the once-fictional ACSL assembly language.

From the [ACSL Wiki](https://www.categories.acsl.org/wiki/index.php?title=Assembly_Language_Programming) -
> ACSL chose to define its own assembly language rather than use a “real” one in order to eliminate the many sticky details associated with real languages. The basic concepts of our ACSL topic description are common to all assembly languages.

ACSL created this language so students would understand how an assembly language may work, but
didn't give them the tools to be able to make programs themselves, which I think is a way easier
way to learn the topic than to do the practice problems they give about reading code.

## How to use

It works similar to how the python shell works. You can enter each command one at a time through the
shell, or you can load a text file.

The ASCL Assembly code file extension is .asmcsl, but it can attempt to run any text file.

| Command | Description |
| --- | --- |
| `HELP` | List all commands |
| `LOADFILE $FILE` | Loads a file |
| `LISTCODE` | Lists all queued statements |
| `RUN` | Runs all queued statements |
| `CLEAR` | Clears all queued statements |
| `EXIT` | Quits the ASMCSL shell |

## Opcodes

Copied from the wiki :)

| ACSL Opcodes | Description |
| --- | --- |
| `LOAD` | The contents of LOC are placed in the ACC. LOC is unchanged. |
| `STORE` | The contents of the ACC are placed in the LOC. ACC is unchanged. |
| `ADD` | The contents of LOC are added to the contents of the ACC. The sum is stored in the ACC. LOC is unchanged. Addition is modulo 1,000,000. |
| `SUB` | The contents of LOC are subtracted from the contents of the ACC. The difference is stored in the ACC. LOC is unchanged. Subtraction is modulo 1,000,000. |
| `MULT` | The contents of LOC are multiplied by the contents of the ACC. The product is stored in the ACC. LOC is unchanged. Multiplication is modulo 1,000,000. |
| `DIV` | The contents of LOC are divided into the contents of the ACC. The signed integer part of the quotient is stored in the ACC. LOC is unchanged. |
| `BG` | Branch to the instruction labeled with LOC if ACC>0. |
| `BE` | Branch to the instruction labeled with LOC if ACC=0. |
| `BL` | Branch to the instruction labeled with LOC if ACC<0. |
| `BU` | Branch to the instruction labeled with LOC. |
| `READ` | Read a signed integer (modulo 1,000,000) into LOC. |
| `PRINT` | Print the contents of LOC. |
| `DC` | The value of the memory word defined by the LABEL field is defined to contain the specified constant. The LABEL field is mandatory for this opcode. The ACC is not modified. |
| `END` | Program terminates. LOC field is ignored and must be empty. |

| Custom Opcodes | Description |
| --- | --- |
| `PRINTVARS` | Prints the values of all stored variables |