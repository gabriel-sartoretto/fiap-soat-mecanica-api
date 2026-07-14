# 1. Provisiona o cluster Kubernetes local usando o Kind
resource "kind_cluster" "ci_cluster" {
  name           = var.cluster_name
  wait_for_ready = true

  kind_config {
    kind        = "Cluster"
    api_version = "kind.x-k8s.io/v1alpha4"

    node {
      role = "control-plane"
    }
    
    # Adicione mais blocos 'node' se precisar de workers
    # node {
    #   role = "worker"
    # }
  }
}

# 2. Lê todos os manifestos YAML da pasta k8s/ (um nível acima de infra/)
data "kubectl_path_documents" "manifests" {
  pattern = "${path.module}/../k8s/*.yaml"
}

# 3. Aplica os manifestos no cluster
resource "kubectl_manifest" "apply_k8s_files" {
  for_each  = toset(data.kubectl_path_documents.manifests.documents)
  yaml_body = each.value

  depends_on = [kind_cluster.ci_cluster]
}