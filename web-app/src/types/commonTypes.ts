// src/types/commonTypes.ts

import { LocalDateTime } from 'js-joda'; // Nếu bạn muốn sử dụng LocalDateTime từ js-joda
export enum ErrorType {
    SINGLE = 'SINGLE',
    MULTIPLE = 'MULTIPLE',
}

export interface ResponseData<T> {
    status: number;
    message: string;
    timestamp: LocalDateTime;
    data?: T;
}

export interface ErrorResponse {
    timestamp: LocalDateTime;
    status: number;
    error: string;
    message: string;
    path: string;
    type?: ErrorType;
}

export interface ErrorResponseMultipleField {
    timestamp: LocalDateTime;
    status: number;
    error: string;
    message: Record<string, string>;
    path: string;
    type?: ErrorType;
}
