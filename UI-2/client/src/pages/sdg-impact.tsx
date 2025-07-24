import { useQuery } from "@tanstack/react-query";
import { Pie, Bar } from "react-chartjs-2";
import { apiService } from "@/lib/api";

export default function SDGImpactDashboard() {
  const { data, isLoading } = useQuery({
    queryKey: ['sdg-impact'],
    queryFn: () => apiService.get('/api/sdg-impact/summary').then(res => res.data),
  });

  if (!data) return <div>Loading SDG impact data...</div>;

  const goals = data.map((item: any) => item.goalName);
  const counts = data.map((item: any) => item.totalActions);
  const colors = ['#ff6384', '#36a2eb', '#ffcd56', '#4bc0c0', '#9966ff'];

  const pieData = {
    labels: goals,
    datasets: [{
      data: counts,
      backgroundColor: colors,
    }]
  };

  const barData = {
    labels: goals,
    datasets: [
      {
        label: 'Jobs Created',
        data: data.map((d: any) => d.totalJobs),
        backgroundColor: '#4bc0c0',
      },
      {
        label: 'Women Empowered',
        data: data.map((d: any) => d.totalWomen),
        backgroundColor: '#ff6384',
      },
      {
        label: 'Savings Growth (₹)',
        data: data.map((d: any) => d.totalSavings),
        backgroundColor: '#9966ff',
      },
    ]
  };

  return (
    <div className="space-y-8">
      <h1 className="text-2xl font-bold">📊 SDG Impact Dashboard</h1>
      <div className="grid md:grid-cols-2 gap-8">
        <div>
          <h2 className="text-lg font-semibold">Impact Distribution</h2>
          <Pie data={pieData} />
        </div>
        <div>
          <h2 className="text-lg font-semibold">Detailed Metrics</h2>
          <Bar data={barData} />
        </div>
      </div>
    </div>
  );
}