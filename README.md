# clj-adventofcode
Advent of code solutions in clojure

To use `helper.clj` you need the session token to make an authenticated request:

1. Login to AoC.
2. Open browser's developer console and navigate to the Network tab.
3. GET any input page, say adventofcode.com/2016/day/1/input, and look at the request headers.
4. Under cookies there will be a session token (search for 'session=').
