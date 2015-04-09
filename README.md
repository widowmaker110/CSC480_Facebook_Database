#CSC480_Facebook_Database

Project Assignemnt Description: "Write Java code to interface with the database you designed in the previous assignment. Your code should have a clean separation between the client (which may be very simple), the in-memory model, and the data access objects used to connect to the back-end database. Choose at least three of your database tables to implement (perhaps in simplified form, in case your design had lots of tables), and make sure the code can handle creating the database from scratch and populating it with some example data."

## Setup
1. Download zip or clone in desktop
2. Create a batch file (.bat) with the following code:
`java -cp "\Program Files\Java\jdk1.8.0_20\db\lib\derby.jar;\Program Files\Java\jdk1.8.0_20\db\lib\derbynet.jar;\Program Files\Java\jdk1.8.0_20\db\lib\derbyclient.jar;\Program Files\Java\jdk1.8.0_20\db\lib\derbytools.jar" org.apache.derby.drda.NetworkServerControl start -h localhost -noSecurityManager` Change the filepath of the Java as needed. Run it an wait for the command prompt to say its ready.
3. Open the project in your IDE (We're using Eclipse: Luna)
4. Run the project

## Usage

TODO: Write usage instructions

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Credits

Alexander Miller: alexandermiller_2016@depauw.edu

Congshu Wang: congshuwang_2015@depauw.edu

## License
The MIT License (MIT)

Copyright (c) 2015 Alexander Miller & Congshu Wang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
