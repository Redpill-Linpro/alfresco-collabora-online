FROM ${docker.nginx.image}:${docker.nginx.version}

ARG GROUPNAME=Alfresco
ARG GROUPID=1000
ARG USERNAME=aca
ARG USERID=33009

WORKDIR /usr/share/nginx/html

COPY nginx.conf cors_support.conf proxy_pass.conf /etc/nginx/

RUN addgroup -g ${GROUPID} ${GROUPNAME} && \
    adduser -S -u ${USERID} -G ${GROUPNAME} -s "/bin/bash" ${USERNAME} && \
    chown -R ${USERNAME}:${GROUPNAME} /usr/share/nginx/html && \
    chown -R ${USERNAME}:${GROUPNAME} /var/cache/nginx && \
    touch /var/run/nginx.pid && \
    chown -R ${USERNAME}:${GROUPNAME} /var/run/nginx.pid

EXPOSE 8080
USER ${USERNAME}
CMD nginx -g 'daemon off;'