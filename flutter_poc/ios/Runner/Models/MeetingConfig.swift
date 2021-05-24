//
//  MeetingConfig.swift
//  zoom_plugin
//
//  Created by Elias Meireles on 28/02/21.
//

import Foundation

struct MeetingConfig: Codable {
    
    let jwtToken: String?
    let appKey: String?
    let appSecret: String?
    let userName: String
    let meetingNumber: String
    let meetingPassword: String
    let enableLog: Bool?
    let enableGenerateDump: Bool?
    let logSize: Int?
    
    init(jwtToken: String?,
         appKey: String?,
         appSecret: String?,
         userName: String,
         meetingNumber: String,
         meetingPassword: String,
         enableLog: Bool?,
         enableGenerateDump: Bool?,
         logSize: Int?) {
        
        self.jwtToken = jwtToken
        self.appKey = appKey
        self.appSecret = appSecret
        self.userName = userName
        self.meetingNumber = meetingNumber
        self.meetingPassword = meetingPassword
        self.enableLog = enableLog
        self.enableGenerateDump = enableGenerateDump
        self.logSize = logSize
    }
}
