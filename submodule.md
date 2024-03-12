# git submodule

> 여러 프로젝트에서 공통으로 사용해야할 설정 파일(라이브러리나 설정 값 등)이 있을 때 A와 B 저장소에 C에서 사용하는 코드의 내용이 중복될 수도 있다. 이 때 서브모듈을 적용한다면 A와 B 저장소에 C 저장소를
> 공동으로 반영시킬 수 있도록 할 수 있는 것이다. 또한 C 저장소에 수정 작업을 진행하면 A와 B 저장소 모두 수정된 C 저장소의 내용을 적용받게 된다.

### submodule 사용

#### 1. 서브모듈 추가

서브모듈을 사용할 프로젝트에서 서브모듈로 저장소 등록

```bash
$ git submodule add {submodule_repository_url}
$ git submodule add https://github.com/minuk926/xit-mens-core.git

# 실행후 .gitmodules 파일이 생성되며, 서브모듈 저장소의 정보가 추가된다.
```

#### 2. 서브모듈 파일(.gitmodules)에 사용할 브랜치 추가

```text
[submodule "xit-mens-core"]
    path = xit-mens-core
    url = https://github.com/minuk926/xit-mens-core.git
    branch = main
```

#### 3. 서브모듈 변경사항 업데이트

```bash
$ git submodule update --remote --merge
```

#### 4. submodule을 사용하는 프로젝트에서 서브모듈 git 관리 대상에서 제외

> 서브모듈은 부모 프로젝트에서 **git 링크(gitlink)**로 표시
> 서브모듈은 부모 프로젝트에서 변경하지 않는다.
> 아래 명령어로 서브모듈을 비활성화 시키면, 부모프로젝트에서 서브모듈을 변경지 못하며, git 관리 대상에서 제외 된다

```bash
$ git config submodule.<name>.active false
$ git config submodule.xit-mens-core.active false
```

#### 5. 서브모듈 사용시 주의사항

> 서브모듈에 변경사항이 생겼다면 **반드시 상위 저장소보다 먼저 변경사항을 Commit & Push하거나 Pull 해야한다**
>
> 만약 상위 저장소를 push/pull한 후 서브모듈(하위 저장소)을 push/pull 하게 된다면 상위 저장소에서 CI 등의 작업을 진행할 때 서브모듈의 변경점을 가져가지 못한다.

### 서브모듈이 포함된 저장소 복제(clone)

> 일반적으로 사용하는 클론 명령어로 해당 프로젝트를 클론한다면, 서브모듈 디렉토리는 빈 디렉토리로 받아오게 된다.
>
> `--recurse-submodules` 옵션을 사용 한다
>

```bash
$ git clone --recurse-submodules {repository_url}
```


