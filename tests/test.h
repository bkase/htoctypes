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