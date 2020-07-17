import os
import json
from git.repo import Repo
from git.repo.fun import is_git_dir

class GitRepository(object):
    def __init__(self, local_path, repo_url, branch='master'):
        self.local_path = local_path
        self.repo_url = repo_url
        self.repo = None
        self.initial(branch)

    def initial(self, branch):
        if not os.path.exists(self.local_path):
            os.makedirs(self.local_path)
        git_local_path = os.path.join(self.local_path, '.git')
        if not is_git_dir(git_local_path):
            self.repo = Repo.clone_from(self.repo_url, to_path=self.local_path, branch=branch)
        else:
            self.repo = Repo(self.local_path)

    def pull(self):
        self.repo.git.pull()

    def branches(self):
        branches = self.repo.remote().refs
        return [item.remote_head for item in branches if item.remote_head not in ['HEAD', ]]

    def commits(self):
        commit_log = self.repo.git.log('--pretty={"commit":"%h","author":"%an","summary":"%s","date":"%cd"}', max_count=50, date='format:%Y-%m-%d %H:%M')
        return [json.loads(item) for item in commit_log.split('\n') ]

    def tags(self):
        return [tag.name for tag in self.repo.tags]

    def change_to_branch(self, branch):
        self.repo.git.checkout(branch)

    def change_to_commit(self, branch, commit):
        self.change_to_branch(branch=branch)
        self.repo.git.reset('--hard', commit)

    def change_to_tag(self, tag):
        self.repo.git.checkout(tag)

if __name__ == '__main__':
    git = GitRepository('/tmp/', 'https://github.com/DoZX/common-tools.git')

