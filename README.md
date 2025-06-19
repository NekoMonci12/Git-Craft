<p align="center">
  <img src="https://cdn.yuemi.org/yuemi/YueMi-Banner-Dark.png" width="320" alt="YueMi Logo"/>
</p>

<p align="center">
  <a href="https://dsc.gg/yuemi">
    <img src="https://img.shields.io/discord/1145991395388162119?label=discord&style=for-the-badge&colorA=19201a&colorB=298046" alt="Discord"/>
  </a>
</p>

---

**Git Craft** is a secure in-game Git command interface for Minecraft (Paper API 1.21.4+), built for developers and server administrators. It allows players to manage Git repositories directly from the game using `/git` commands with stored encrypted credentials.

---

## 💡 Features

- Perform Git actions from in-game: `init`, `add`, `commit`, `push`, `pull`, `fetch`, `remote`, and more.
- Secure credential storage using **H2 encrypted database** (`CIPHER=AES`).
- Salted token obfuscation for added protection.
- Configurable warning system to prevent unsafe usage of public Git hosting.
- Asynchronous command handling to avoid server lag.

---

## 🛡️ Security Warning

By default, GitCraft **blocks** adding public Git hosting (e.g., GitHub, GitLab) as remote origins.  
To override this, set `disable-unsafe-warning: true` in `plugins/GitCraft/config.yml`.

We **strongly recommend** using a **self-hosted Git platform** such as:
- [Gitea](https://gitea.io/)
- [Forgejo](https://forgejo.org/)
- [GitBucket](https://gitbucket.github.io/)

---

## ⚙️ Configuration

**`plugins/GitCraft/config.yml`**:

```yaml
# WARNING: Disabling this allows users to push to public Git hosts (GitHub, GitLab, etc.)
disable-unsafe-warning: false
