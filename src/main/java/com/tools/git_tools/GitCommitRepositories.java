package com.tools.git_tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class GitCommitRepositories {

    public static void main(String[] args) {
        String rootDirectory = Utils.getTempPath();
        String commitMessage = "[SRVCHNCOMP-1119] Multideploy - Removendo Id de idempotência do cabeçalho"; // Mensagem do commit
        File rootDir = new File(rootDirectory);

        if (rootDir.isDirectory()) {
            try {
                processDirectories(rootDir, commitMessage);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("O caminho especificado não é um diretório válido.");
        }
    }

    public static void processDirectories(File rootDir, String commitMessage) throws IOException, InterruptedException {
        Stack<File> directoriesStack = new Stack<>();
        directoriesStack.push(rootDir);

        while (!directoriesStack.isEmpty()) {
            File currentDir = directoriesStack.pop();

            if (currentDir.isDirectory()) {
                // Verificar se é um repositório Git
                if (isGitRepository(currentDir)) {
                    System.out.println("Encontrado repositório Git em: " + currentDir.getAbsolutePath());
                    executeGitPull(currentDir);
                    executeGitAdd(currentDir);
                    executeGitCommit(currentDir, commitMessage);
                    executeGitPush(currentDir);
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
        printProcessOutput(process);
        process.waitFor();
    }

    private static void executeGitAdd(File directory) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("git", "add", ".");
        processBuilder.directory(directory);
        Process process = processBuilder.start();
        printProcessOutput(process);
        process.waitFor();
    }

    private static void executeGitCommit(File directory, String commitMessage) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("git", "commit", "-m", commitMessage);
        processBuilder.directory(directory);
        Process process = processBuilder.start();
        printProcessOutput(process);
        process.waitFor();
    }

    private static void executeGitPush(File directory) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("git", "push");
        processBuilder.directory(directory);
        Process process = processBuilder.start();
        printProcessOutput(process);
        process.waitFor();
    }

    private static void printProcessOutput(Process process) throws IOException {
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
    }
}
