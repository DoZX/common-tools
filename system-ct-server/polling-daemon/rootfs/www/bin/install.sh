#!/bin/bash

PYTHON_PACKAGE_PATH='/www/bin/zip/python'

install_python_packages() {
    /usr/bin/pip install --upgrade $PYTHON_PACKAGE_PATH/pip-20.1.1-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/certifi-2020.6.20-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/urllib3-1.25.10-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/idna-2.10-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/chardet-3.0.4-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/requests-2.24.0-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/configparser-4.0.2-py2.py3-none-any.whl && \
    /usr/bin/pip install $PYTHON_PACKAGE_PATH/PyYAML-5.3.1.tar.gz
}

main() {
  install_python_packages
  echo 'install success!'
}

main