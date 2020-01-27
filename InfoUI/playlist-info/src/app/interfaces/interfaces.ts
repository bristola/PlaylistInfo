export interface IAuthorizeResponse {
    accessToken: string;
    refreshToken: string;
}

export interface ISimplePlaylist {
    name: string;
    createdBy: string;
    spotifyUrl: string;
    imageUrl: string;
    isCollaborative: boolean;
    isPublic: boolean;
}