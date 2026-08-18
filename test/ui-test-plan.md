# Quu — UI Test Plan

Test cases for the `test-ui` skill. The runner reads this file, starts the
program once per test case, feeds each command in order, and compares the
output that follows each command against the expected output recorded here.

## Session block format

Each test case has one fenced ` ```session ` block.

- A line beginning with `> ` is **input** typed at the prompt.
- Every other line is **expected output** for the input immediately above it.
- Lines appearing *before* the first `> ` are the expected **startup** output.
  Omit them to skip checking the banner and greeting.

## Comparison rules

Output is normalised on both sides before comparing, so decoration does not
affect the result:

1. Leading and trailing whitespace is stripped from every line.
2. Blank lines are ignored.
3. Divider lines — 10 or more consecutive `_` or `-` characters — are ignored.
4. An expected line of exactly `...` matches zero or more output lines.

Everything else must match exactly, including spacing *inside* a line. So
`[T][ ] read book` and `[T] [ ] read book` are different, and the test fails.

Rules 1–3 exist because Level 0 states the horizontal lines are optional; they
are presentation, not behaviour. Rule 4 covers the banner, which Level 0 also
makes optional and whose content is the author's choice.

## Source of expected outputs

Expected outputs are taken from the iP specification's sample sessions
(Levels 0–4). Where the specification does not define a message — malformed
input, out-of-range indices — the test case is marked **project convention**
and records the message this project has chosen. Those may be changed freely;
the spec-derived ones may not.

## Running

```
java test/TestRunner.java
```

Exits `0` if every case passes, `1` on the first failure.

---

## TC-01 — Greet the user and exit

**Aim:** On startup the program greets the user, and `bye` prints the farewell and terminates.

**Source:** iP spec, Level 0.

```session
...
Hello! I'm Quu.
What can I do for you?
> bye
Bye. Hope to see you again soon!
```

---

## TC-02 — Add a todo

**Aim:** `todo` creates a ToDo, confirms with the `[T]` type icon and an unticked status box, and reports the new task count.

**Source:** iP spec, Level 4.

```session
> todo borrow book
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
```

---

## TC-03 — Add a deadline

**Aim:** `deadline … /by …` splits the description from the due date and renders it as `[D]` with the date in parentheses.

**Source:** iP spec, Level 4.

```session
> deadline return book /by Sunday
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
```

---

## TC-04 — Add an event

**Aim:** `event … /from … /to …` splits the description, start and end, and renders it as `[E]` with both times in one bracketed clause.

**Source:** iP spec, Level 4.

```session
> event project meeting /from Mon 2pm /to 4pm
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
```

---

## TC-05 — List tasks in insertion order

**Aim:** `list` numbers tasks from 1 in the order they were added, with each task's type and status icons. Guards against the ordering being decided by the storage structure rather than insertion.

**Source:** iP spec, Level 4.

```session
> todo read book
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
> deadline return book /by June 6th
Got it. I've added this task:
[D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
> event project meeting /from Aug 6th 2pm /to 4pm
Got it. I've added this task:
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
> list
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

---

## TC-06 — Mark a task as done

**Aim:** `mark N` sets the status of the Nth listed task and echoes it with `[X]`.

**Source:** iP spec, Level 3.

```session
> todo read book
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
> mark 1
Nice! I've marked this task as done:
[T][X] read book
> list
Here are the tasks in your list:
1.[T][X] read book
```

---

## TC-07 — Unmark a task

**Aim:** `unmark N` clears the status and reports it with the *not done* wording, distinct from the `mark` message.

**Source:** iP spec, Level 3.

```session
> todo read book
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
> mark 1
Nice! I've marked this task as done:
[T][X] read book
> unmark 1
OK, I've marked this task as not done yet:
[T][ ] read book
```

---

## TC-08 — Numbering survives non-adding commands

**Aim:** Commands that add nothing — `list`, an unknown word — must not consume a task number. Guards against the counter tracking inputs rather than tasks.

**Source:** iP spec, Level 4 (implied by `mark N` addressing listed positions).

```session
> todo read book
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
> list
Here are the tasks in your list:
1.[T][ ] read book
> todo return book
Got it. I've added this task:
[T][ ] return book
Now you have 2 tasks in the list.
> list
Here are the tasks in your list:
1.[T][ ] read book
2.[T][ ] return book
```

---

## TC-09 — Mark an out-of-range index

**Aim:** An index with no matching task is reported, not crashed on.

**Source:** project convention — not specified by the iP.

```session
> todo read book
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
> mark 99
Invalid task
```

---

## TC-10 — Mark a non-numeric index

**Aim:** A non-numeric argument is reported, not crashed on.

**Source:** project convention — not specified by the iP.

```session
> mark abc
Not a task number
```

---

## TC-11 — Mark with no argument

**Aim:** A bare `mark` with no argument is reported, not crashed on. Regression guard for the `parts[1]` out-of-bounds crash.

**Source:** project convention — not specified by the iP.

```session
> mark
Which task?
```

---

## TC-12 — Unrecognised command

**Aim:** Input that is not a known command is rejected rather than silently stored as a task.

**Source:** project convention — not specified by the iP.

```session
> blah
Invalid command
```

---

## TC-13 — Malformed deadline

**Aim:** A `deadline` missing its `/by` clause reports the expected format.

**Source:** project convention — not specified by the iP.

```session
> deadline return book
format as deadline <task> /by <time>
```

---

## TC-14 — Malformed event

**Aim:** An `event` missing its `/to` clause reports the expected format.

**Source:** project convention — not specified by the iP.

```session
> event project meeting /from Mon 2pm
format as event <task> /from <start> /to <end>
```
