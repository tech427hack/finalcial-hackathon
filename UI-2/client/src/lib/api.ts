import { apiRequest } from "./queryClient";
import type {
  User, Role, Group, Member, SavingDeposit, LoanApplication, Meeting, Poll,
  SDGMapping, SDGImpact,
  InsertUser, InsertRole, InsertGroup, InsertMember, InsertSavingDeposit,
  InsertLoanApplication, InsertMeeting, InsertPoll, InsertSDGMapping
} from "@shared/schema";

export class APIService {
  // --------------------- Auth ---------------------
  static async login(username: string, password: string) {
    const response = await apiRequest("POST", "/api/auth/login", { username, password });
    return response.data;
  }

  // --------------------- Users ---------------------
  static async getUser(id: number): Promise<User> {
    const response = await apiRequest("GET", `/api/users/${id}`);
    return response.data;
  }

  static async createUser(data: InsertUser): Promise<User> {
    const response = await apiRequest("POST", "/api/users", data);
    return response.data;
  }

  // --------------------- Roles ---------------------
  static async getRoles(): Promise<Role[]> {
    const response = await apiRequest("GET", "/api/roles");
    return response.data;
  }

  static async getRole(id: number): Promise<Role> {
    const response = await apiRequest("GET", `/api/roles/${id}`);
    return response.data;
  }

  static async createRole(data: InsertRole): Promise<Role> {
    const response = await apiRequest("POST", "/api/roles", data);
    return response.data;
  }

  static async updateRole(id: number, data: Partial<InsertRole>): Promise<Role> {
    const response = await apiRequest("PUT", `/api/roles/${id}`, data);
    return response.data;
  }

  static async deleteRole(id: number): Promise<void> {
    await apiRequest("DELETE", `/api/roles/${id}`);
  }

  // --------------------- Groups ---------------------
  static async getGroups(): Promise<Group[]> {
    const response = await apiRequest("GET", "/api/groups");
    return response.data;
  }

  static async getGroup(id: number): Promise<Group> {
    const response = await apiRequest("GET", `/api/groups/${id}`);
    return response.data;
  }

  static async createGroup(data: InsertGroup): Promise<Group> {
    const response = await apiRequest("POST", "/api/groups", data);
    return response.data;
  }

  static async updateGroup(id: number, data: Partial<InsertGroup>): Promise<Group> {
    const response = await apiRequest("PUT", `/api/groups/${id}`, data);
    return response.data;
  }

  static async deleteGroup(id: number): Promise<void> {
    await apiRequest("DELETE", `/api/groups/${id}`);
  }

  // --------------------- Members ---------------------
  static async getMembers(groupId?: number): Promise<Member[]> {
  const url = groupId ? `/api/members?groupId=${groupId}` : "/api/members";
  const response = await apiRequest("GET", url);
  const data = await response.data;

  return data.map((m: any) => ({
    id: m.id,
    name: m.name,
    phone: m.phone,
    email: m.email,
    gender: m.gender,
    aadhaar: m.aadhaar,
    groupId: m.groupId,
    isApproved: m.approved,
    groupName: m.groupName ?? '', 
    roleName: m.roleName ?? '',
  }));
}

  static async getMember(id: number): Promise<Member> {
    const response = await apiRequest("GET", `/api/members/${id}`);
    return response.data;
  }

  static async createMember(data: InsertMember): Promise<Member> {
    const response = await apiRequest("POST", "/api/members", data);
    return response.data;
  }

  static async updateMember(id: number, data: Partial<InsertMember>): Promise<Member> {
    const response = await apiRequest("PUT", `/api/members/${id}`, data);
    return response.data;
  }

  static async deleteMember(id: number): Promise<void> {
    await apiRequest("DELETE", `/api/members/${id}`);
  }

  // --------------------- Savings ---------------------
  static async getSavingDeposits(memberId?: number, groupId?: number): Promise<SavingDeposit[]> {
    const params = new URLSearchParams();
    if (memberId) params.append("memberId", memberId.toString());
    if (groupId) params.append("groupId", groupId.toString());

    const url = `/api/savings${params.toString() ? `?${params.toString()}` : ""}`;
    const response = await apiRequest("GET", url);
    return response.data;
  }

  static async createSavingDeposit(data: InsertSavingDeposit): Promise<SavingDeposit> {
    const response = await apiRequest("POST", "/api/savings", data);
    return response.data;
  }

  // --------------------- Loans ---------------------
  static async getLoanApplications(memberId?: number, groupId?: number, status?: string): Promise<LoanApplication[]> {
    const params = new URLSearchParams();
    if (memberId) params.append("memberId", memberId.toString());
    if (groupId) params.append("groupId", groupId.toString());
    if (status) params.append("status", status);

    const url = `/api/loans${params.toString() ? `?${params.toString()}` : ""}`;
    const response = await apiRequest("GET", url);
    return response.data;
  }

  static async getLoanApplication(id: number): Promise<LoanApplication> {
    const response = await apiRequest("GET", `/api/loans/${id}`);
    return response.data;
  }

  static async createLoanApplication(data: InsertLoanApplication): Promise<LoanApplication> {
    const response = await apiRequest("POST", "/api/loans/apply", data);
    return response.data;
  }

  static async updateLoanApplication(id: number, data: Partial<LoanApplication>): Promise<LoanApplication> {
    const response = await apiRequest("PUT", `/api/loans/${id}`, data);
    return response.data;
  }

  // --------------------- Meetings ---------------------
  static async getMeetings(groupId: number): Promise<Meeting[]> {
    const response = await apiRequest("GET", `/api/meetings?groupId=${groupId}`);
    return response.data;
  }

  static async createMeeting(data: InsertMeeting): Promise<Meeting> {
    const response = await apiRequest("POST", "/api/meetings", data);
    return response.data;
  }

  // --------------------- Polls ---------------------
  static async getPolls(groupId: number): Promise<Poll[]> {
    const response = await apiRequest("GET", `/api/polls?groupId=${groupId}`);
    return response.data;
  }

  static async createPoll(data: InsertPoll): Promise<Poll> {
    const response = await apiRequest("POST", "/api/polls", data);
    return response.data;
  }

  static async updatePoll(id: number, data: Partial<Poll>): Promise<Poll> {
    const response = await apiRequest("PUT", `/api/polls/${id}`, data);
    return response.data;
  }

  // --------------------- SDG ---------------------
  static async getSDGMappings(): Promise<SDGMapping[]> {
    const response = await apiRequest("GET", "/api/sdg/mappings");
    return response.data;
  }

  static async createSDGMapping(data: InsertSDGMapping): Promise<SDGMapping> {
    const response = await apiRequest("POST", "/api/sdg/mappings", data);
    return response.data;
  }

  static async getSDGImpacts(groupId: number): Promise<SDGImpact[]> {
    const response = await apiRequest("GET", `/api/sdg/impact/${groupId}`);
    return response.data;
  }
}

export { APIService as apiService };