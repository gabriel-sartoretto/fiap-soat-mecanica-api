output "kubeconfig" {
  value       = kind_cluster.ci_cluster.kubeconfig
  description = "Kubeconfig do cluster gerado"
  sensitive   = true
}