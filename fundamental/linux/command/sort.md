# sort

> sort는 텍스트로 된 파일의 행단위 정렬을 할 때 사용하는 명령어이다.  
> 특정 DB나 프로그램, 쉘 프로그램등의 입력값으로 사용되는 데이터를 직접 정렬 또는 편집 할 때 편리하게 사용할 수 있다.  
> 출처: https://webdir.tistory.com/153 [WEBDIR]

사용법 : sort [옵션] 파일명

오름차순 정렬
```
sort textfile
```

내림차순 정렬
```
sort -r textfile
```

지정한 두번째 필드 (-k 옵션)를 기준으로 정렬
```
sort -k 2 textfile
```

중복된 내용을 하나로 취급하여 유일 정렬
```
sort -u textfile
```

용량 크기 순으로 오름차순 정렬
```
ls -l /var/log | sort -k 5
```

파일 이름을 대상으로 오름차순 정렬
```
ls -l /var/log | sort -k 8
```

현재 디렉터리 용량 크기 순으로 오름차순 정렬 ( --threshold=1G를 넣으면 1기가 이상 용량을 가지는 디렉터리 한정 )
```
du -h --threshold=1G --max-depth=1 --time ./ | awk '{printf ("%s\t%s %s\t%s\n", $1,$2,$3,$4 )|"sort -k2"}'
```


참고
```
$ sort -h
^C
jira@works:~$ sort --help
Usage: sort [OPTION]... [FILE]...
  or:  sort [OPTION]... --files0-from=F
Write sorted concatenation of all FILE(s) to standard output.

 

With no FILE, or when FILE is -, read standard input.

 

Mandatory arguments to long options are mandatory for short options too.
Ordering options:

 

  -b, --ignore-leading-blanks  ignore leading blanks
  -d, --dictionary-order      consider only blanks and alphanumeric characters
  -f, --ignore-case           fold lower case to upper case characters
  -g, --general-numeric-sort  compare according to general numerical value
  -i, --ignore-nonprinting    consider only printable characters
  -M, --month-sort            compare (unknown) < 'JAN' < ... < 'DEC'
  -h, --human-numeric-sort    compare human readable numbers (e.g., 2K 1G)
  -n, --numeric-sort          compare according to string numerical value
  -R, --random-sort           shuffle, but group identical keys.  See shuf(1)
      --random-source=FILE    get random bytes from FILE
  -r, --reverse               reverse the result of comparisons
      --sort=WORD             sort according to WORD:
                                general-numeric -g, human-numeric -h, month -M,
                                numeric -n, random -R, version -V
  -V, --version-sort          natural sort of (version) numbers within text

 

Other options:

 

      --batch-size=NMERGE   merge at most NMERGE inputs at once;
                            for more use temp files
  -c, --check, --check=diagnose-first  check for sorted input; do not sort
  -C, --check=quiet, --check=silent  like -c, but do not report first bad line
      --compress-program=PROG  compress temporaries with PROG;
                              decompress them with PROG -d
      --debug               annotate the part of the line used to sort,
                              and warn about questionable usage to stderr
      --files0-from=F       read input from the files specified by
                            NUL-terminated names in file F;
                            If F is - then read names from standard input
  -k, --key=KEYDEF          sort via a key; KEYDEF gives location and type
  -m, --merge               merge already sorted files; do not sort
  -o, --output=FILE         write result to FILE instead of standard output
  -s, --stable              stabilize sort by disabling last-resort comparison
  -S, --buffer-size=SIZE    use SIZE for main memory buffer
  -t, --field-separator=SEP  use SEP instead of non-blank to blank transition
  -T, --temporary-directory=DIR  use DIR for temporaries, not $TMPDIR or /tmp;
                              multiple options specify multiple directories
      --parallel=N          change the number of sorts run concurrently to N
  -u, --unique              with -c, check for strict ordering;
                              without -c, output only the first of an equal run
  -z, --zero-terminated     line delimiter is NUL, not newline
      --help     display this help and exit
      --version  output version information and exit

 

KEYDEF is F[.C][OPTS][,F[.C][OPTS]] for start and stop position, where F is a
field number and C a character position in the field; both are origin 1, and
the stop position defaults to the line's end.  If neither -t nor -b is in
effect, characters in a field are counted from the beginning of the preceding
whitespace.  OPTS is one or more single-letter ordering options [bdfgiMhnRrV],
which override global ordering options for that key.  If no key is given, use
the entire line as the key.  Use --debug to diagnose incorrect key usage.

 

SIZE may be followed by the following multiplicative suffixes:
% 1% of memory, b 1, K 1024 (default), and so on for M, G, T, P, E, Z, Y.

 

*** WARNING ***
The locale specified by the environment affects sort order.
Set LC_ALL=C to get the traditional sort order that uses
native byte values.

 

GNU coreutils online help: <http://www.gnu.org/software/coreutils/>
Full documentation at: <http://www.gnu.org/software/coreutils/sort>
or available locally via: info '(coreutils) sort invocation'
```
