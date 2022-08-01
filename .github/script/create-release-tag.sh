#!/usr/bin/env bash

######################################################################
# An script helps us trigger the create-release workflow
#
# How to use it
# ./create-release-tag.sh --release-tag 1.0.0
#
######################################################################

# The script is a local script file. So we can not use Github-secrets,
# And this token is the key to access the Github-API, should be maintained by the manager.
GITHUB_TOKEN="xxx"
workflow_event_type="create-release"
build_legal_parameters=()
build_legal_parameters+=("--release-tag")

global_release_tag=""

list_contains() {
  find_var=$1
  shift
  array=("$@")
  result=false
  for var in "${array[@]}"; do
    if [[ "$find_var" == "$var" ]]; then
      result=true
      break
    fi
  done
  echo $result
}

function check_running_workflow(){
  sleep 1
  local repo_owner=$1
  local repo_name=$2
  local retry_times=$3
  running_workflow_json=$(curl -s \
    -H "Authorization: token $GITHUB_TOKEN" \
    https://api.github.com/repos/"$repo_owner"/"$repo_name"/actions/runs)
  workflow_runs_length=$(echo "$running_workflow_json" | jq -r '.workflow_runs | length')
  running_workflows=0
  for (( i = 0; i < $((workflow_runs_length)); i++ )); do
    id=$(echo "$running_workflow_json" | jq -r ".workflow_runs[$i].id")
    status=$(echo "$running_workflow_json" | jq -r ".workflow_runs[$i].status")
    workflow_name=$(echo "$running_workflow_json" | jq -r ".workflow_runs[$i].name")
    if [[ "$status" == "in_progress" || "$status" == "queued" ]] ; then
      workflow_url=$(echo "$running_workflow_json" | jq -r ".workflow_runs[$i].html_url")
      running_workflows=$((running_workflows++))
      echo "Found running workflow:$workflow_name."
      if [ "$workflow_name" == "$workflow_event_type" ]; then
        echo "Found the running workflow: $workflow_url"
        exit 0
      fi
    fi
  done
  if ((running_workflows == 0)) && ((retry_times > 0)); then
    retry_times=$((retry_times-1))
    check_running_workflow "$repo_owner" "$repo_name" $retry_times
  else
    echo "Can not found the running workflow, there maybe some network error."
  fi
}

request_github_api_to_create_test_pr() {
  local current_key=""
  #Define an array to store all the parameters
  local debug_parameters=()
  local parameters=()
  local should_be_value=false
  for arg in "${@:1}"; do
    if [[ $arg == -* ]] || [[ $arg == --* ]]; then
      if [ $should_be_value == true ]; then
        # case 1: more parameters with one value
        # abt -t -t task
        echo "Should be value, but get parameter:$arg"
        exit 1
      else
        should_be_value=true
        current_key=$arg
        # case 4: check the parameter is available.
        # abt -unknown xxx
        is_legal_parameter=$(list_contains "$current_key" "${build_legal_parameters[@]}")
        if [ "$is_legal_parameter" != true ]; then
          echo "Should enter a legal parameter name, Can not found the parameter name: '$current_key'."
          exit 1
        fi
      fi
    else
      if [ $should_be_value == false ]; then
        # case 2: one parameter with more values
        # abt -t task task
        echo "Should be parameter name, but get value:$arg"
        exit 1
      else
        should_be_value=false
        if [[ $current_key == "-d" ]] || [[ $current_key == "--debug" ]]; then
          debug_parameters+=($arg)
        else
          parameters+=($current_key:$arg)
        fi
      fi
    fi
  done
  if [ $should_be_value == true ]; then
    # case 5: no value for the key
    # abt build -t
    echo "Should be the parameter value, but there is no value!"
    exit 1
  fi
  local GIT_USER_NAME=$(git config user.name)
  local GIT_USER_EMAIL=$(git config user.email)

  local repository_url="https://api.github.com/repos/JackChen365/android-sample-library/dispatches"
  for entry in "${parameters[@]}"; do
    key=${entry%%:*}
    value=${entry#*:}
    case "$key" in
    "--release-tag")
      global_release_tag=$value
      ;;
    esac
  done
  # Initial the default parameter by debug parameters.
  for entry in "${debug_parameters[@]}"; do
    key=${entry%%=*}
    value=${entry#*=}
    case "$key" in
    "branch")
      git_branch=$value
      ;;
    "repo")
      repository_url=$value
      ;;
    esac
  done
  echo "######################################################################"
  echo "repository_url: $repository_url"
  echo "######################################################################"

  echo "Start requesting the Github Actions API to update the app property."
  # request the GithubActionAPI to update the App versionName and versionCode
  http_status=$(curl \
    -X POST \
    -H "Accept: application/vnd.github.v3+json" \
    -H "authorization: Bearer $GITHUB_TOKEN" \
    "$repository_url" \
      -d '{"event_type": "create-release",
      "client_payload": {"release_tag": "'"$global_release_tag"'",
      "user_name": "'"${GIT_USER_NAME}"'",
      "user_email": "'"${GIT_USER_EMAIL}"'"
      }}' -o /dev/null -w '%{http_code}\n' -s)

  echo "http_status:$http_status"
  if [[ ${http_status} -lt 200 || ${http_status} -gt 299 ]] ; then
    echo "The request has sent failed. Please check your network."
  else
    repo_owner=$(echo "$repository_url" | cut -d/ -f5)
    repo_name=$(echo "$repository_url" | cut -d/ -f6)
    echo "repo_owner:$repo_owner repo_name:$repo_name"
    check_running_workflow "$repo_owner" "$repo_name" 5
    echo "The request has sent successful. Please waiting for the response."
  fi
}

request_github_api_to_create_test_pr "$@"