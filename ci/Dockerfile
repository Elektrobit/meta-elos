FROM ubuntu:jammy

ARG USER=ci
ARG UID=1000
ARG GID=1000

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
 && apt-get install -y \
    build-essential \
    cpio \
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

RUN git clone -b kirkstone git://git.yoctoproject.org/poky.git /home/$USER/layers/poky/
RUN git clone -b kirkstone git://git.openembedded.org/meta-openembedded /home/$USER/layers/meta-openembedded/

RUN echo ". /home/$USER/layers/poky/oe-init-build-env /base/build >/dev/null" >> /home/$USER/.bashrc

CMD /bin/bash
