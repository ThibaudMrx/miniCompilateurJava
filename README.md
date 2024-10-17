# MiniJava Project

Projet étudiant de création d'un mini compilateur java, comprennant lexing, parsing, jusqu'à la génération d'un exéctuable

This repository contains the MiniJava project, featuring input files and build configurations.

## Project Structure

```plaintext
miniJava/
├── .metadata/
│   ├── .lock
│   ├── .log
│   ├── .plugins/
│   └── version.ini
│
├── fr.n7.stl.block/
│   ├── build.xml
│   ├── input.txt
│   ├── input01_1.txt
│   ├── input01_2.txt
│   ├── input01_3.txt
│   ├── input01_4.txt
│   ├── input01_5.txt
│   ├── input01_6.txt
│   ├── input01_7.txt
│   ├── input01_8.txt
│   ├── input01_9.txt
│   ├── input02.txt
│   ├── input03.txt
│   ├── input04.txt
│   ├── input05.txt
│   ├── input06.txt
│   ├── input07.txt
│   ├── input08.txt
│   ├── input09.txt
│   ├── input10.txt
│   ├── input11.txt
│   ├── input12.txt
│   ├── input13.txt
│   ├── input14.txt
│   ├── input2.txt
│   └── input3.txt
│
├── idees.txt
└── question_memoire.txt
```

### Files and Directories

- **.metadata/**: Contains metadata and internal configuration files for the project.
- **fr.n7.stl.block/**: This directory houses the main project files and build scripts.
  - **build.xml**: Ant build script to compile and execute the project.
  - **input.txt**, **input01_1.txt**, ..., **input14.txt**, **input2.txt**, **input3.txt**: Input data files used for various project tests.
- **idees.txt**: Brainstorming file containing ideas related to the project.
- **question_memoire.txt**: Notes or questions relevant to the project documentation.

## Building the Project

To build and run the project, use [Apache Ant](https://ant.apache.org/). Follow these steps:

1. **Navigate to the Project Directory**:

    ```bash
    cd fr.n7.stl.block
    ```

2. **Execute the Build Script**:

    ```bash
    ant -f build.xml
    ```

This will execute the build process defined in `build.xml`.

## License

This project is licensed under the [MIT License](LICENSE).
