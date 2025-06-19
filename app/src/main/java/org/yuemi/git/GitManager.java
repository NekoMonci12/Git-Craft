package org.yuemi.git;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.AddCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GitManager {

    private final File repoDir;
    private Git git;

    public GitManager(File repoDir) {
        this.repoDir = repoDir;
    }

    private Git getOrOpenGit() throws Exception {
        if (git == null) {
            git = Git.open(repoDir);
        }
        return git;
    }

    public boolean isCloned() {
        return new File(repoDir, ".git").exists();
    }

    public void cloneRepo(String url) throws Exception {
        this.git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(repoDir)
                .call();
    }

    public void addAll() throws Exception {
        getOrOpenGit().add().addFilepattern(".").call();
    }

    public void addFiles(List<String> files) throws Exception {
        AddCommand add = getOrOpenGit().add();
        for (String file : files) {
            add.addFilepattern(file);
        }
        add.call();
    }

    public void commit(String message) throws Exception {
        getOrOpenGit()
                .commit()
                .setMessage(message)
                .setAuthor("Minecraft", "mc@localhost")
                .call();
    }

    public void push(String username, String token) throws Exception {
        getOrOpenGit()
                .push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, token))
                .call();
    }

    public void checkout(String branch) throws Exception {
        getOrOpenGit()
                .checkout()
                .setName(branch)
                .call();
    }

    public void close() {
        if (git != null) {
            git.close();
        }
    }

    public void reset(ResetType type) throws Exception {
        getOrOpenGit().reset().setMode(type).call();
    }

    public void resetFile(String filePath) throws Exception {
        getOrOpenGit().reset().addPath(filePath).call();
    }

    public void initRepo(JavaPlugin plugin) throws Exception {
        if (git == null) {
            git = Git.init().setDirectory(repoDir).call();
        }

        File gitignore = new File(repoDir, ".gitignore");
        if (!gitignore.exists()) {
            try (InputStream in = plugin.getResource("gitignore.txt")) {
                if (in != null) {
                    Files.copy(in, gitignore.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }


    public void addRemote(String url) throws Exception {
        getOrOpenGit().remoteAdd()
            .setName("origin")
            .setUri(new org.eclipse.jgit.transport.URIish(url))
            .call();
    }

    public void setRemoteUrl(String url) throws Exception {
        getOrOpenGit().remoteSetUrl()
            .setRemoteName("origin")
            .setRemoteUri(new org.eclipse.jgit.transport.URIish(url))
            .call();
    }

    public void removeRemote() throws Exception {
        getOrOpenGit().remoteRemove()
            .setRemoteName("origin")
            .call();
    }

    public void fetch(String username, String token) throws Exception {
        getOrOpenGit()
            .fetch()
            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, token))
            .call();
    }

    public void pull(String username, String token) throws Exception {
        getOrOpenGit()
            .pull()
            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, token))
            .call();
    }

    public Status getStatus() throws Exception {
        return getOrOpenGit().status().call();
    }

}
