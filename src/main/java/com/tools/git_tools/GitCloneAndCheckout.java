package com.tools.git_tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class GitCloneAndCheckout {
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


    public static void cloneAndCheckout(List<String> directories, String sshUrl) {
        for (String dir : directories) {
            try {
                // Criar o diretório se ele não existir
                File directory = new File(dir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Extrair o nome do repositório a partir da URL SSH
                String repoName = sshUrl.substring(sshUrl.lastIndexOf("/") + 1, sshUrl.lastIndexOf(".git"));
                File repoDirectory = new File(directory, repoName);

                // Processo de clonagem
                ProcessBuilder cloneProcessBuilder = new ProcessBuilder("git", "clone", sshUrl, repoDirectory.getAbsolutePath());
                cloneProcessBuilder.directory(directory);
                Process cloneProcess = cloneProcessBuilder.start();

                // Exibir a saída do processo de clonagem
                printProcessOutput(cloneProcess);

                cloneProcess.waitFor();

                // Verifica se o repositório foi clonado corretamente
                if (new File(repoDirectory, ".git").exists()) {
                    // Processo de checkout para a branch develop
                    ProcessBuilder checkoutProcessBuilder = new ProcessBuilder("git", "checkout", "develop");
                    checkoutProcessBuilder.directory(repoDirectory);
                    Process checkoutProcess = checkoutProcessBuilder.start();

                    // Exibir a saída do processo de checkout
                    printProcessOutput(checkoutProcess);

                    checkoutProcess.waitFor();

                    System.out.println("Repository cloned and checked out to develop branch in directory: " + repoDirectory.getAbsolutePath());
                } else {
                    System.err.println("Failed to clone repository or '.git' directory not found in: " + repoDirectory.getAbsolutePath());
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.err.println("Failed to clone or checkout repository in directory: " + dir);
            }
        }
    }

    public static void main(String[] args) {

        String sshUrl = "git@gitlab.gastecnologia.com.br:topazmultichannel/topaz/cdk/workspace/multichannel/multichannel-investments/multichannel-investment-fixed-income/multichannel-investment-fixed-income-deposit.git";
        // Example usage
        List<String> directories = List.of(
                Utils.getTempPath(),
                Utils.getAbsolutePath()

        );

        cloneAndCheckout(directories, sshUrl);
    }
}

