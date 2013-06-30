# H to js-ctypes transpiler v0.1

A transpiler that takes C header files as input and outputs valid [js-ctypes](https://developer.mozilla.org/en-US/docs/Mozilla/js-ctypes) type declarations in JavaScript.

### How to run

```bash
$ sbt run file.h [file2.h ...]
```

### Example

```
$ cat test.h

typedef int z;
typedef struct yo { int z; } pro;
typedef struct { int z; } pro2;
typedef int x;
typedef x y;

int some_function(char x, int y);
void another_function();

struct input {
  int x;
  int y;
  const int big;

  int (*fn1)(int *, char**);
  int (*fn2)();
};

typedef input input_sym;

$ sbt run test.h

const z = ctypes.int;
const pro = new ctypes.StructType("yo", [
  { "z": ctypes.int }
]);
const pro2 = new ctypes.StructType("pro2", [
  { "z": ctypes.int }
]);
const x = ctypes.int;
const y = x;
const some_function = ctypes.declare("some_function", ctypes.default_abi, ctypes.int, ctypes.char, ctypes.int);
const another_function = ctypes.declare("another_function", ctypes.default_abi, ctypes.void_t);
const input = new ctypes.StructType("input", [
  { "x": ctypes.int },
  { "y": ctypes.int },
  { "big": ctypes.int },
  { "fn1": ctypes.FunctionType(ctypes.default_abi, ctypes.int, ctypes.int.ptr, ctypes.char.ptr.ptr) },
  { "fn2": ctypes.FunctionType(ctypes.default_abi, ctypes.int) }
]);
const input_sym = input;
```

### Contributing

Pull-requests welcome

