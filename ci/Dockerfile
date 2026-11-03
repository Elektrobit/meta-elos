FROM ubuntu:jammy
ARG UBUNTU_MIRROR="https://ftp.gwdg.de/pub/linux/debian/ubuntu/"

RUN apt-get update && apt-get reinstall ca-certificates -y
RUN sed -i "s,http://archive.ubuntu.com/ubuntu/,$UBUNTU_MIRROR," /etc/apt/sources.list \
 && sed -i "s,http://security.ubuntu.com/ubuntu/,$UBUNTU_MIRROR," /etc/apt/sources.list \
 && cat /etc/apt/sources.list

ARG USER=ci
ARG UID=1000
ARG GID=1000

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
 && apt-get install -y \
    build-essential \
    cpio \
    curl \
    diffstat chrpath \
    file \
    gawk wget \
    git \
    iproute2 \
    iptables \
    iputils-ping \
    libncurses-dev \
    libssl-dev \
    locales \
    lz4 \
    openssl \
    python3 \
    python3-distutils \
    python3-jinja2 \
    python3-pip \
    python3-setuptools \
    screen \
    sudo \
    vim \
    yapf3 \
    zstd

SHELL [ "/bin/bash", "-c" ]

RUN locale-gen en_US.UTF-8 \
 && groupadd -g $GID -o ci \
 && useradd -m -u $UID -g $GID -o -s /bin/bash $USER \
 && echo "$USER ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers


WORKDIR /base

USER $USER
ENV LC_ALL=en_US.UTF-8
ENV DOCKERBUILD=1
ENV BBCACHE="/home/ci/bbcache"
ENV PROJECT_LAYER_DIR="/home/$USER/layers"

RUN git clone --depth 1 -b kirkstone https://git.yoctoproject.org/poky.git ${PROJECT_LAYER_DIR}/poky/
RUN git clone --depth 1 -b kirkstone https://git.openembedded.org/meta-openembedded ${PROJECT_LAYER_DIR}/meta-openembedded/
RUN ln -s /base ${PROJECT_LAYER_DIR}/meta-elos

RUN echo ". /home/$USER/layers/poky/oe-init-build-env /base/build"  >> /home/$USER/.bashrc \
    && echo 'export BB_ENV_PASSTHROUGH_ADDITIONS="${BB_ENV_PASSTHROUGH_ADDITIONS} PROJECT_LAYER_DIR"' >> /home/$USER/.bashrc

CMD /bin/bash
