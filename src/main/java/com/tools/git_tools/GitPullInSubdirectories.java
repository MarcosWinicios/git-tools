package com.tools.git_tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class GitPullInSubdirectories {
    public static final String PATH = Utils.getTempPath();
    public static final String PATH2 = Utils.getAbsolutePath();

    public static void main(String[] args) {

        File rootDir = new File(PATH);

        if (rootDir.isDirectory()) {
            try {
                processDirectories(rootDir);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("O caminho especificado não é um diretório válido.");
        }
    }


    public static void processDirectories(File rootDir) throws IOException, InterruptedException {
        Stack<File> directoriesStack = new Stack<>();
        directoriesStack.push(rootDir);

        while (!directoriesStack.isEmpty()) {
            File currentDir = directoriesStack.pop();

            if (currentDir.isDirectory()) {
                // Verificar se é um repositório Git
                if (isGitRepository(currentDir)) {
                    System.out.println("Encontrado repositório Git em: " + currentDir.getAbsolutePath());
                    executeGitPull(currentDir);
                }

                // Adicionar subdiretórios à pilha
                File[] subDirs = currentDir.listFiles(File::isDirectory);
                if (subDirs != null) {
                    for (File subDir : subDirs) {
                        directoriesStack.push(subDir);
                    }
                }
            }
        }
    }

    private static boolean isGitRepository(File directory) {
        File gitDir = new File(directory, ".git");
        return gitDir.exists() && gitDir.isDirectory();
    }

    private static void executeGitPull(File directory) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("git", "pull");
        processBuilder.directory(directory);
        Process process = processBuilder.start();

        // Exibir a saída do processo
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
        }

        process.waitFor();
    }
}

