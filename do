#!/usr/bin/env bash
{ # Boilerplate
  set -e

  # Ensure we have associative arrays, coprocesses, mapfile and more
  if test "${BASH_VERSINFO[0]}" -lt 4; then exit 1; fi

  # Needed by the `usage` command
  readonly __functions_before="$(declare -F | cut -d' ' -f3)"

  readonly HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null 2>&1 && pwd)"

  usage() {
    echo Available commands:
    echo
    comm -13 <(echo "$__functions_before") <(echo "$__functions_after") | sed '/^__/d;s/^/    - /'
  }

  here() {
    ( cd "$HERE" && "$@" )
  }
}

__with-app-env() {
    SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/haunt \
    SPRING_DATASOURCE_USERNAME=admin \
    SPRING_DATASOURCE_PASSWORD=ghost \
    SPRING_PROFILES_ACTIVE="local" \
    "$@"
}

 start-local(){
   docker run --name haunt_db -d -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=ghost -e POSTGRES_DB=haunt -p 5432:5432 postgres:10
    __with-app-env gw bootRun

   docker rm -f haunt_db || true
 }

{ # Boilerplate
  # Needed by the `usage` command
  readonly __functions_after="$(declare -F | cut -d' ' -f3)"

  "${@:-usage}"
}